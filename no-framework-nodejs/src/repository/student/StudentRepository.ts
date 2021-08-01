import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {toInsertQuery, toUpdateQuery} from "../../config/orm/objectToSql";
import {transform} from "../../config/orm/transform";
import {Student} from "../../entity/student/Student";
import {BaseRepository} from "../BaseRepository";

@Service()
export class StudentRepository extends BaseRepository {

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate);
    }

    async isSignup (studentId: number): Promise<boolean> {
        const items = await this.nodePgTemplate.query(`SELECT 1 FROM student WHERE id = '${studentId}' limit 1`);
        return items[0];
    }

    async findOne (studentId: number): Promise<Student> {
        // noinspection SqlResolve
        const items = await this.nodePgTemplate.query(`SELECT * FROM student WHERE id = '${studentId}'`);
        return transform(items[0], Student);
    }
}
