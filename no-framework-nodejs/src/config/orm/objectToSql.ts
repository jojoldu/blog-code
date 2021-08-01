import {BaseEntity} from "../../entity/BaseEntity";
import {EntityProperties} from "./EntityProperties";
import {camelToSnake} from "./converter/camelToSnake";

export function toUpsertQuery<T extends BaseEntity>(entity: T): string {
    if(entity.isInsertTarget()) {
        return toInsertQuery(entity);
    }

    return toUpdateQuery(entity);
}

export function toSelectAllQuery<T extends BaseEntity>(type: { new(): T }): string {
    const tableName = getTableNameByType(type);
    return `SELECT * FROM ${tableName}`;
}

export function toSelectOneQuery<T extends BaseEntity>(type: { new(): T }, id: number): string {
    if(!id) {
        throw new Error('1개의 Entity를 조회하기 위해서는 ID는 필수값입니다.');
    }
    const tableName = getTableNameByType(type);
    return `SELECT * FROM ${tableName} WHERE id=${id}`;
}

export function toInsertQuery<T extends BaseEntity>(entity: T): string {
    entity.renewCreatedAt(new Date());
    const tableName = getTableName(entity);
    const entityProperties = new EntityProperties(entity);
    const columnNames = entityProperties.getColumnNamesString();
    const columnValues = entityProperties.getInsertValuesString();
    return `INSERT INTO ${tableName}(${columnNames}) VALUES(${columnValues}) RETURNING id`;
}

export function toUpdateQuery<T extends BaseEntity>(entity: T): string {
    entity.validateExistId();
    entity.renewUpdatedAt(new Date());
    const tableName = getTableName(entity);
    const setQuery = new EntityProperties(entity).getUpdateValuesString();
    return `UPDATE ${tableName} SET ${setQuery} WHERE id=${entity.id} RETURNING id`;
}

export function toDeleteQuery<T extends BaseEntity>(entity: T): string {
    entity.validateExistId();
    const tableName = getTableName(entity);

    return `DELETE FROM ${tableName} WHERE id=${entity.id}`;
}

function getTableNameByType<T extends BaseEntity>(type: { new(): T ;}): string {
    return camelToSnake(type.name);
}

function getTableName<T extends BaseEntity>(entity: T): string {
    return camelToSnake(entity.constructor.name);
}
