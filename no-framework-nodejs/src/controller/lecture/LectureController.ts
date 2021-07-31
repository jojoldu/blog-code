import {Service} from "typedi";
import {BaseResponse} from "../BaseResponse";
import {LectureService} from "../../service/lecture/LectureService";
import {LecturesRequest} from "./dto/LecturesRequest";
import {LectureCreateRequest} from "./dto/LectureCreateRequest";
import {LectureUpdateRequest} from "./dto/LectureUpdateRequest";

@Service()
export class LectureController {

    constructor(private lectureService: LectureService) {
    }

    async getLectures(reqParams) {
        try {
            const params = LecturesRequest.byQueryParam(reqParams);
            const body = await this.lectureService.getLectures(params);
            return BaseResponse.OK(body);
        } catch (e) {
            console.error(`getLectures Error=${reqParams}`, e);
            return BaseResponse.ERROR(e);
        }
    }

    async getLecture(reqParams) {
        try {
            const lectureId = reqParams.lectureId;
            const result = await this.lectureService.getLecture(lectureId);
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`getLecture Error=${reqParams}`, e);
            return BaseResponse.ERROR(e);
        }
    }

    async create(reqBody) {
        try {
            const result = await this.lectureService.create(LectureCreateRequest.byReqBody(reqBody));
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`create Error=${reqBody}`, e);
            return BaseResponse.ERROR(e);
        }
    }

    async update(lectureId:number, reqBody) {
        try {
            const result = await this.lectureService.update(lectureId, LectureUpdateRequest.byReqBody(reqBody));
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`update Error=${reqBody}`, e);
            return BaseResponse.ERROR(e);
        }
    }

    async register(lectureId:number, reqBody) {
        try {
            const studentId = reqBody.studentId;
            const lectureId = reqBody.lectureId;
            const result = await this.lectureService.register(studentId, lectureId);
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`register Error=${reqBody}`, e);
            return BaseResponse.ERROR(e);
        }
    }

    async publish(lectureId: number) {
        try {
            const result = await this.lectureService.publish(lectureId);
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`publish Error=${lectureId}`, e);
            return BaseResponse.ERROR(e);
        }
    }
}
