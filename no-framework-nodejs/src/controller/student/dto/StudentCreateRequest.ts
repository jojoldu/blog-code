import {Student} from "../../../entity/student/Student";

export class StudentCreateRequest {
    private _name: string;
    private _email: string;

    constructor() {
    }

    static byReqBody(reqBody) {
        const result = new StudentCreateRequest();
        result._name = reqBody.name;
        result._email = reqBody.email;

        return result;
    }

    toEntity(): Student {
        return Student.signup(
            this._name,
            this._email
        );
    }

    get email(): string {
        return this._email;
    }
}
