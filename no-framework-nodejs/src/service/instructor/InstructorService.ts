import {Service} from "typedi";

@Service()
export class InstructorService {

    constructor() {
    }

    getInstructors() {
        return ['a','b','c'];
    }


}
