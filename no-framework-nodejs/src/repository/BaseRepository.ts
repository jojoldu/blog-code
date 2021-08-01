import {BaseEntity} from "../entity/BaseEntity";
import {toInsertQuery, toSelectAllQuery, toSelectOneQuery, toUpdateQuery} from "../config/orm/objectToSql";
import {NodePgTemplate} from "../config/database/NodePgTemplate";
import {transform} from "../config/orm/transform";

export class BaseRepository<T extends BaseEntity> {
    constructor(protected nodePgTemplate: NodePgTemplate, private type: { new(): T }) {}

    async findAll(): Promise<T[]> {
        const query = toSelectAllQuery(this.type);
        const items = await this.nodePgTemplate.query(query);
        return items.map(item => transform(item, this.type));
    }

    async findOne(id:number): Promise<T> {
        const query = toSelectOneQuery(this.type, id);
        const item = await this.nodePgTemplate.query(query);
        return transform(item[0], this.type);
    }

    async insert(entity: T): Promise<number> {
        const query = toInsertQuery(entity);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }

    async update(entity: T): Promise<number> {
        const query = toUpdateQuery(entity);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }
}
