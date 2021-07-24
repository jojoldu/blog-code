export class InstructorsRequest {
    private readonly _name: string;
    private readonly _lectureName: string;
    private readonly _studentId: number;

    constructor(query) {
        this._name = query.name;
        this._lectureName = query.lectureName;
        this._studentId = query.studentId;
    }

    get name(): string {
        return this._name;
    }

    get lectureName(): string {
        return this._lectureName;
    }

    get studentId(): number {
        return this._studentId;
    }
}
