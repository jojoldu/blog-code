import { Service } from "typedi";
import { NodePgTemplate } from "../../config/database/NodePgTemplate";
import { LecturesRequest } from "../../controller/lecture/dto/LecturesRequest";
import { LecturesItem } from "./dto/LecturesItem";
import { LectureItem } from "./dto/LectureItem";
import { Lecture } from "../../entity/lecture/Lecture";
import { transform } from "../../config/orm/transform";
import { NumberUtil } from "../../util/NumberUtil";
import { BaseRepository } from "../BaseRepository";
import { convert, LocalDate, LocalDateTime } from '@js-joda/core';
import { LectureSearchDto } from './dto/LectureSearchDto';

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

    async getLectureByDate (createdAt: Date): Promise<LectureItem> {
        // noinspection SqlResolve
        const lectures = await this.nodePgTemplate.queryWith(
          'SELECT * FROM lecture l WHERE l.created_at >= $1',
          [createdAt]
        );

        return transform(lectures[0], LecturesItem);
    }

    async getLectureByLocalDate (createdAt: LocalDateTime): Promise<LectureItem> {
        // noinspection SqlResolve
        const createdDate = convert(createdAt).toDate();
        const lectures = await this.nodePgTemplate.queryWith(
          'SELECT * FROM lecture l WHERE l.created_at >= $1',
          [createdDate]
        );

        return transform(lectures[0], LecturesItem);
    }

    async getLectureByDto (param: LectureSearchDto): Promise<LectureItem> {
        // noinspection SqlResolve
        const lectures = await this.nodePgTemplate.queryWith(
          'SELECT * FROM lecture l WHERE l.created_at >= $1',
          [param.createdAt]
        );

        return transform(lectures[0], LecturesItem);
    }
}
