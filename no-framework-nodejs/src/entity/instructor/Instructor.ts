import {BaseEntity} from "../BaseEntity";

export class Instructor extends BaseEntity {
    name: string;
    email: string;
    companyName: string;

    constructor() {
        super();
    }

    static signup(name: string, email: string, companyName: string): Instructor {
        const instructor = new Instructor();
        instructor.name = name;
        instructor.email = email;
        instructor.companyName = companyName;

        return instructor;
    }
}
