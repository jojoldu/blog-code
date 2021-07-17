import {EntityProperty} from "./EntityProperty";

export class EntityProperties {
    properties: Array<EntityProperty>;

    constructor(entity) {
        const keys = Object.getOwnPropertyNames(entity);
        this.properties = keys.map(name => new EntityProperty(name, entity[name]));
    }
}
