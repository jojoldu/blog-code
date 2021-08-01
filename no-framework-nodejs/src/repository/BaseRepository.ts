import {BaseEntity} from "../entity/BaseEntity";
import {toInsertQuery, toSelectAllQuery, toUpdateQuery} from "../config/orm/objectToSql";
import {NodePgTemplate} from "../config/database/NodePgTemplate";

export class BaseRepository {

    constructor(protected nodePgTemplate: NodePgTemplate) {}
    //
    // async findAll<T extends BaseEntity>(type: { new(): T ;}): Promise<T[]> {
    //     const query = toSelectAllQuery(type);
    //     const result = await this.nodePgTemplate.query(query);
    // }

    async insert<T extends BaseEntity>(entity: T): Promise<number> {
        const query = toInsertQuery(entity);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }

    async update<T extends BaseEntity>(entity: T): Promise<number> {
        const query = toUpdateQuery(entity);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }
}
