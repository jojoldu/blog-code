import {Service} from "typedi";
import {InstructorService} from "../../service/instructor/InstructorService";

@Service()
export class InstructorController {

    constructor(private instructorService: InstructorService) {}

    getInstructors() {
        return this.instructorService.getInstructors();
    }
}
