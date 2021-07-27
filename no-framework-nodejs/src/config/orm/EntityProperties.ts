import {EntityProperty} from "./EntityProperty";

export class EntityProperties {
    properties: Array<EntityProperty>;

    constructor(entity) {
        const keys = Object.getOwnPropertyNames(entity);
        this.properties = keys.map(name => new EntityProperty(name, entity[name]));
    }

    getColumnNamesString() {
        return this.properties
            .map(p => p.getColumnName())
            .join(',');
    }

    getInsertValuesString() {
        return this.properties
            .map(p => p.getColumnValue())
            .join(',');
    }

    getUpdateValuesString() {
        return this.properties
            .map(p => `${p.getColumnName()}=${p.value}`)
            .join(', ');
    }
}
