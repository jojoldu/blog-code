import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {LecturesItem} from "./dto/LecturesItem";
import {LectureItem} from "./dto/LectureItem";
import {StudentLectureMap} from "../../entity/student/StudentLectureMap";
import {toInsertQuery, toUpdateQuery} from "../../config/orm/objectToSql";
import {Lecture} from "../../entity/lecture/Lecture";
import {transform} from "../../config/orm/sqlToObject";
import {NumberUtil} from "../../util/NumberUtil";

@Service()
export class LectureRepository {

    constructor(private nodePgTemplate: NodePgTemplate) {}

    async getLectures (param: LecturesRequest) {
        const queryBody = `FROM lecture ${param.getWhereCondition()}`;
        const count = await this.nodePgTemplate.query(`SELECT COUNT(*) ${queryBody}`);
        const items = await this.nodePgTemplate.query(`SELECT * ${queryBody} ${param.getPageQuery()} ${param.getOrderByQuery()}`);

        return {
            items: items.map(d => transform(d, LecturesItem)),
            count: NumberUtil.toNumber(count[0].count)
        }
    }

    async getLecture (lectureId: number): Promise<LectureItem> {
        const lecture:Lecture = await this.findEntity(lectureId);
        // noinspection SqlResolve
        const students = await this.nodePgTemplate.query(
            `SELECT s.name, m.created_at FROM student_lecture_map m 
            JOIN student s ON m.student_id = s.id 
            WHERE m.lecture_id = '${lectureId}'`
        );

        return new LectureItem(lecture, students);
    }

    async findEntity (lectureId: number): Promise<Lecture>  {
        // noinspection SqlResolve
        const items = await this.nodePgTemplate.query(`SELECT * FROM lecture WHERE id = '${lectureId}'`);
        return transform(items[0], Lecture);
    }

    async insertStudentLectureMap (studentLectureMap :StudentLectureMap) {
        const query = toInsertQuery(studentLectureMap);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }

    async insert (lecture: Lecture) {
        const query = toInsertQuery(lecture);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }

    async update(lecture: Lecture) {
        const query = toUpdateQuery(lecture);
        const result = await this.nodePgTemplate.query(query);
        return result[0].id;
    }
}
