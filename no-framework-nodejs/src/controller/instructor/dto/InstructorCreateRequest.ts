import {Instructor} from "../../../entity/instructor/Instructor";

export class InstructorCreateRequest {
    private _name: string;
    private _email: string;
    private _companyName: string;

    constructor() {
    }

    static byReqBody(reqBody) {
        const result = new InstructorCreateRequest();
        result._name = reqBody.name;
        result._email = reqBody.email;
        result._companyName = reqBody.companyName;

        return result;
    }

    toEntity(): Instructor {
        return Instructor.signup(
            this._name,
            this._email,
            this._companyName
        );
    }

    get email(): string {
        return this._email;
    }
}
