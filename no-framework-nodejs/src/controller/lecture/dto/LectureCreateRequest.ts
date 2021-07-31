import {LectureCategory} from "../../../entity/lecture/LectureCategory";
import {Lecture} from "../../../entity/lecture/Lecture";

export class LectureCreateRequest {
    private _name: string;
    private _description: string;
    private _category: LectureCategory;
    private _price: number;
    private _instructorId: number;

    constructor() {
    }

    static byReqBody(reqBody) {
        const result = new LectureCreateRequest();
        result._name = reqBody.name;
        result._description = reqBody.description;
        result._category = reqBody.category;
        result._price = reqBody.price;
        result._instructorId = reqBody.instructorId;

        return result;
    }

    toEntity(): Lecture {
        return Lecture.create(
            this._name,
            this._description,
            this._category,
            this._price,
            this._instructorId
        );
    }
}
