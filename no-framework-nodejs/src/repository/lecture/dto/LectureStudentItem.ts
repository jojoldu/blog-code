export class LectureStudentItem {
    name: string;
    createdAt: string;

    constructor(queryResult) {
        this.name = queryResult.name;
        this.createdAt = queryResult.created_at;
    }
}
