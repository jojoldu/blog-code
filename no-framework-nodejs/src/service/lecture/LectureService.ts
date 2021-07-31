import {Service} from "typedi";
import {LectureRepository} from "../../repository/lecture/LectureRepository";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {Page} from "../Page";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {LectureCreateRequest} from "../../controller/lecture/dto/LectureCreateRequest";
import {LectureUpdateRequest} from "../../controller/lecture/dto/LectureUpdateRequest";

@Service()
export class LectureService {

    constructor(
        private lectureRepository: LectureRepository,
        private nodePgTemplate: NodePgTemplate
        ) {
    }

    async getLectures (param: LecturesRequest) {
        const {items, count} = await this.lectureRepository.getLectures(param);
        return new Page(count, param.getPageSize(), items);
    }

    async getLecture(lectureId: number) {
        return await this.lectureRepository.getLecture(lectureId);
    }

    async create (param: LectureCreateRequest) {
        return await this.lectureRepository.insert(param.toEntity());
    }

    async update (lectureId: number, param: LectureUpdateRequest) {
        const lecture = await this.lectureRepository.findEntity(lectureId);
        lecture.updateContent(
            param.name,
            param.description,
            param.category,
            param.price
        )
        return await this.lectureRepository.update(lecture);
    }

    async register (studentId: number, lectureId: number) {
        const lecture = await this.lectureRepository.findEntity(lectureId);
        const studentLectureMap = lecture.register(studentId);
        if(!studentLectureMap) {
            throw new Error("공개 강좌가 아닙니다.");
        }

        const poolClient = await this.nodePgTemplate.startTransaction();
        try {
            await this.lectureRepository.update(lecture);
            await this.lectureRepository.insertStudentLectureMap(studentLectureMap);
            await this.nodePgTemplate.commit(poolClient);
        } catch (e) {
            await this.nodePgTemplate.rollback(poolClient);
        }
    }

    async publish (lectureId: number) {
        const lecture = await this.lectureRepository.findEntity(lectureId);
        lecture.publish();

        const poolClient = await this.nodePgTemplate.startTransaction();

        try{
            await this.lectureRepository.update(lecture);
            await this.nodePgTemplate.commit(poolClient);
        } catch (e) {
          await this.nodePgTemplate.rollback(poolClient);
        }

    }

}
