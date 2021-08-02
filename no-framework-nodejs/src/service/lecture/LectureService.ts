import {Service} from "typedi";
import {LectureRepository} from "../../repository/lecture/LectureRepository";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {Page} from "../Page";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {LectureCreateRequest} from "../../controller/lecture/dto/LectureCreateRequest";
import {LectureUpdateRequest} from "../../controller/lecture/dto/LectureUpdateRequest";
import {StudentLectureMapRepository} from "../../repository/student/StudentLectureMapRepository";

@Service()
export class LectureService {

    constructor(
        private nodePgTemplate: NodePgTemplate,
        private lectureRepository: LectureRepository,
        private studentLectureMapRepository:StudentLectureMapRepository
        ) {
    }

    async getLectures (param: LecturesRequest) {
        const {items, count} = await this.lectureRepository.getLectures(param);
        return new Page(count, param.pageSize, items);
    }

    async getLecture(lectureId: number) {
        return await this.lectureRepository.getLecture(lectureId);
    }

    async create (param: LectureCreateRequest) {
        return await this.lectureRepository.insert(param.toEntity());
    }

    async update (lectureId: number, param: LectureUpdateRequest) {
        const lecture = await this.lectureRepository.findOne(lectureId);
        lecture.updateContent(
            param.name,
            param.description,
            param.category,
            param.price
        )
        return await this.lectureRepository.update(lecture);
    }

    async register (studentId: number, lectureId: number) {
        const lecture = await this.lectureRepository.findOne(lectureId);
        const studentLectureMap = lecture.register(studentId);

        const poolClient = await this.nodePgTemplate.startTransaction();
        try {
            await this.lectureRepository.update(lecture);
            await this.studentLectureMapRepository.insert(studentLectureMap);
            await this.nodePgTemplate.commit(poolClient);
        } catch (e) {
            await this.nodePgTemplate.rollback(poolClient);
            throw e;
        }
    }

    async publish (lectureId: number) {
        const lecture = await this.lectureRepository.findOne(lectureId);
        lecture.publish();

        const poolClient = await this.nodePgTemplate.startTransaction();

        try{
            await this.lectureRepository.update(lecture);
            await this.nodePgTemplate.commit(poolClient);
        } catch (e) {
          await this.nodePgTemplate.rollback(poolClient);
          throw e;
        }

    }

}
