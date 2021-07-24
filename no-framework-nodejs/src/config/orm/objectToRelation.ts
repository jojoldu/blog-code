import {BaseEntity} from "../../entity/BaseEntity";
import {EntityProperties} from "./EntityProperties";
import {camelToSnake} from "./camelToSnake";

export function toUpsertQuery<T extends BaseEntity>(entity: T): string {
    if(entity.isInsertTarget()) {
        return toInsertQuery(entity);
    }

    return toUpdateQuery(entity);
}

export function toInsertQuery<T extends BaseEntity>(entity: T): string {
    const tableName = getTableName(entity);
    const entityProperties = new EntityProperties(entity);
    const columnNames = entityProperties.getColumnNamesString();
    const columnValues = entityProperties.getInsertValuesString();
    return `insert into ${tableName}(${columnNames}) values(${columnValues})`;
}

export function toUpdateQuery<T extends BaseEntity>(entity: T): string {
    entity.validateExistId();
    entity.renewUpdateAt();
    const tableName = getTableName(entity);
    const setQuery = new EntityProperties(entity).getUpdateValuesString();
    return `update ${tableName} set ${setQuery} where id=${entity.id}`;
}

export function toDeleteQuery<T extends BaseEntity>(entity: T): string {
    entity.validateExistId();
    const tableName = getTableName(entity);

    return `delete from ${tableName} where id=${entity.id}`;
}

function getTableName(entity) {
    return camelToSnake(entity.constructor.name);
}
