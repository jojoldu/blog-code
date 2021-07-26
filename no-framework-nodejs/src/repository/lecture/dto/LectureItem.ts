import {LectureCategory} from "../../../entity/lecture/LectureCategory";

export class LectureItem {
    readonly name: string;
    readonly description: string;
    readonly category: LectureCategory;
    readonly price: number;
    readonly studentCount: number;
    readonly createdAt: string;
    readonly updatedAt: string;
    readonly stuents: object[];

    constructor(queryResult, studentQueryResult) {
        this.name = queryResult.name;
        this.description = queryResult.description;
        this.category = queryResult.category;
        this.price = queryResult.price;
        this.studentCount = queryResult.student_count;
        this.createdAt = queryResult.created_at;
        this.updatedAt = queryResult.updated_at;

    }

}
