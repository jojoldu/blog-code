import {BaseEntity} from "../../entity/BaseEntity";
import {EntityProperties} from "./EntityProperties";
import {camelToSnake} from "./converter/camelToSnake";

export function toUpsertQuery<T extends BaseEntity>(entity: T): string {
    if(entity.isInsertTarget()) {
        return toInsertQuery(entity);
    }

    return toUpdateQuery(entity);
}

export function toSelectAllQuery<T extends BaseEntity>(entity: T): string {
    const tableName = getTableName(entity);
    return `SELECT * FROM ${tableName}`;
}

export function toSelectOneQuery<T extends BaseEntity>(entity: T): string {
    entity.validateExistId();
    const tableName = getTableName(entity);
    return `SELECT * FROM ${tableName} WHERE id='${entity.id}'`;
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

function getTableName(entity) {
    return camelToSnake(entity.constructor.name);
}
