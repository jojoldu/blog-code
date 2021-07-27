import {Service} from "typedi";
import {NodeTemplate} from "../../config/database/NodeTemplate";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {LecturesItem} from "./dto/LecturesItem";
import {LectureItem} from "./dto/LectureItem";
import {StudentLectureMap} from "../../entity/student/StudentLectureMap";
import {toInsertQuery} from "../../config/orm/objectToRelation";
import {Lecture} from "../../entity/lecture/Lecture";

@Service()
export class LectureRepository {

    constructor(private nodeTemplate: NodeTemplate) {}

    async getLectures (param: LecturesRequest) {
        const queryBody = this.getLecturesQuery(param);
        const count = await this.nodeTemplate.query(`SELECT COUNT(1) ${queryBody}`);
        const items = await this.nodeTemplate.query(`SELECT * ${queryBody}`);
        return {
            items: items.map(d => new LecturesItem(d)),
            count: parseInt(count[0].count)
        }
    }

    private getLecturesQuery (param: LecturesRequest): string {
        const condition = param.getWhereCondition();
        return `FROM lecture ${condition} ${param.getPageQuery()}`;
    }

    async getLecture (lectureId: number): Promise<LectureItem> {
        const items = await this.nodeTemplate.query(`SELECT * FROM lecture WHERE id = '${lectureId}'`);
        const students = await this.nodeTemplate.query(
            `SELECT s.name, m.created_at FROM student_lecture_map m 
            JOIN student s ON m.student_id = s.id 
            WHERE s.lecture_id = '${lectureId}'`
        );

        return new LectureItem(items[0], students);
    }

    async findEntity (lectureId: number): Promise<Lecture>  {
        const items = await this.nodeTemplate.query(`SELECT * FROM lecture WHERE id = '${lectureId}'`);
        return Lecture.byJson(items[0]);
    }

    async register (studentLectureMap :StudentLectureMap) {
        const query = toInsertQuery(studentLectureMap);
        return await this.nodeTemplate.query(query);
    }
}
