import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {toInsertQuery, toUpdateQuery} from "../../config/orm/objectToSql";
import {transform} from "../../config/orm/transform";
import {Student} from "../../entity/student/Student";
import {BaseRepository} from "../BaseRepository";

@Service()
export class StudentRepository extends BaseRepository<Student> {

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate, Student);
    }

    async isSignup (studentId: number): Promise<boolean> {
        const items = await this.nodePgTemplate.query(`SELECT 1 FROM student WHERE id = '${studentId}' limit 1`);
        return items[0];
    }
}
