import {LectureCategory} from "../../../entity/lecture/LectureCategory";
import {Lecture} from "../../../entity/lecture/Lecture";

export class LectureUpdateRequest {
    private _name: string;
    private _description: string;
    private _category: LectureCategory;
    private _price: number;

    constructor() {
    }

    static byReqBody(reqBody) {
        const result = new LectureUpdateRequest();
        result._name = reqBody.name;
        result._description = reqBody.description;
        result._category = reqBody.category;
        result._price = reqBody.price;

        return result;
    }

    get name(): string {
        return this._name;
    }

    get description(): string {
        return this._description;
    }

    get category(): LectureCategory {
        return this._category;
    }

    get price(): number {
        return this._price;
    }
}
