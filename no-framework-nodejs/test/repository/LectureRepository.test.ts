import {LectureRepository} from "../../src/repository/lecture/LectureRepository";
import {NodeTemplate} from "../../src/config/database/NodeTemplate";

describe('LectureRepository', () => {
    let lectureRepository:LectureRepository;

    beforeEach(() => {
        lectureRepository = new LectureRepository(new MockNodeTemplate());
    });

    it('findEntity', () => {

    });
});


class MockNodeTemplate extends NodeTemplate {

    constructor() {
        super();
    }

    init() {}

    async query(jsonObj: string): Promise<any[]> {
        return new Promise(JSON.parse(jsonObj));
    }
}


