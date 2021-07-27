import {Service} from "typedi";
import {InstructorService} from "../../service/instructor/InstructorService";

@Service()
export class InstructorController {

    constructor(private instructorService: InstructorService) {}

    async getNow() {
        return await this.instructorService.getNow();
    }

    getInstructors() {
        return this.instructorService.getInstructors();
    }
}
