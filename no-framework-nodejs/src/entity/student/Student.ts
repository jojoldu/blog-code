import {BaseEntity} from "../BaseEntity";

export class Student extends BaseEntity {
    name: string;
    email: string;

    constructor() {
        super();
    }

    static signup (name: string, email: string): Student {
        const student = new Student();
        student.name = name;
        student.email = email;
        return student;
    }


}
