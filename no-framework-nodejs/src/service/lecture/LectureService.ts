import {Service} from "typedi";
import {LectureRepository} from "../../repository/lecture/LectureRepository";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {Page} from "../Page";
import {NodeTemplate} from "../../config/database/NodeTemplate";

@Service()
export class LectureService {

    constructor(
        private lectureRepository: LectureRepository,
        private nodeTemplate: NodeTemplate
        ) {
    }

    async getLectures (param: LecturesRequest) {
        const {items, count} = await this.lectureRepository.getLectures(param);
        return new Page(count, param.getPageSize(), items);
    }

    async getLecture(lectureId: number) {
        return await this.lectureRepository.getLecture(lectureId);
    }

    async register (studentId: number, lectureId: number) {
        const lecture = await this.lectureRepository.findEntity(lectureId);
        const studentLectureMap = lecture.register(studentId);
        if(!studentLectureMap) {
            throw new Error("공개 강좌가 아닙니다.");
        }

        const poolClient = await this.nodeTemplate.startTransaction();
        try {
            await this.lectureRepository.update(lecture);
            await this.lectureRepository.insertStudentLectureMap(studentLectureMap);
            await this.nodeTemplate.commit(poolClient);
        } catch (e) {
            await this.nodeTemplate.rollback(poolClient);
        }

    }

    async publish (lectureId: number) {
        const lecture = await this.lectureRepository.findEntity(lectureId);
        lecture.publish();

        const poolClient = await this.nodeTemplate.startTransaction();

        try{
            await this.lectureRepository.update(lecture);
            await this.nodeTemplate.commit(poolClient);
        } catch (e) {
          await this.nodeTemplate.rollback(poolClient);
        }

    }

}
