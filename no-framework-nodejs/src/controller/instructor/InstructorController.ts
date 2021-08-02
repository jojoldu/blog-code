import {Service} from "typedi";
import {InstructorService} from "../../service/instructor/InstructorService";
import {BaseResponse} from "../BaseResponse";
import {InstructorCreateRequest} from "./dto/InstructorCreateRequest";

@Service()
export class InstructorController {

    constructor(private instructorService: InstructorService) {}

    async getNow() {
        return await this.instructorService.getNow();
    }

    getInstructors() {
        return this.instructorService.getInstructors();
    }

    async signup(reqBody) {
        try {
            const result = await this.instructorService.signup(InstructorCreateRequest.byReqBody(reqBody));
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`signup Error=${reqBody}`, e);
            return BaseResponse.ERROR(e);
        }
    }
}
