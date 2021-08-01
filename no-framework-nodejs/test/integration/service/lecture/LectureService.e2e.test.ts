import {TestSupportRepository} from "../../TestSupportRepository";
import {Container} from "typedi";
import {TestLectureCreator} from "../../TestLectureCreator";

describe('LectureService', () => {
    const testSupportRepository: TestSupportRepository = Container.get(TestSupportRepository);
    const testLectureCreator: TestLectureCreator = Container.get(TestLectureCreator);

    beforeAll(async () => {
        await testSupportRepository.dropAll();
        await testSupportRepository.createTable();
    });

    afterEach(async () => {
        await testSupportRepository.deleteAll();
    });

    afterAll(async () => {
        await testSupportRepository.closeConnection();
    });

    it('update', () => {
    });

});
