import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {LecturesItem} from "./dto/LecturesItem";
import {LectureItem} from "./dto/LectureItem";
import {StudentLectureMap} from "../../entity/student/StudentLectureMap";
import {toInsertQuery} from "../../config/orm/objectToSql";
import {Lecture} from "../../entity/lecture/Lecture";
import {transform} from "../../config/orm/transform";
import {NumberUtil} from "../../util/NumberUtil";
import {BaseRepository} from "../BaseRepository";
import {StudentLectureMapRepository} from "../student/StudentLectureMapRepository";

@Service()
export class LectureRepository extends BaseRepository<Lecture> {

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate, Lecture);
    }

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
        const lecture:Lecture = await this.findOne(lectureId);
        // noinspection SqlResolve
        const students = await this.nodePgTemplate.query(
            `SELECT s.name, m.created_at FROM student_lecture_map m 
            JOIN student s ON m.student_id = s.id 
            WHERE m.lecture_id = '${lectureId}'`
        );

        return new LectureItem(lecture, students);
    }
}
