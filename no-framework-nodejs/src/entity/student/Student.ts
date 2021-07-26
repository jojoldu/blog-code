import {BaseEntity} from "../BaseEntity";

export class Student extends BaseEntity {
    readonly name: string;
    readonly email: string;

    constructor(name: string, email: string) {
        super();
        this.name = name;
        this.email = email;
    }

    static signup (name: string, email: string): Student {
        return new Student (name, email);
    }

}
