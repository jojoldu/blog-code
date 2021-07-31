import {LectureRepository} from "../../src/repository/lecture/LectureRepository";
import {NodePgTemplate} from "../../src/config/database/NodePgTemplate";
import {anyString, instance, mock, when} from "ts-mockito";
import {LectureCategory} from "../../src/entity/lecture/LectureCategory";

describe('LectureRepository', () => {
    let lectureRepository:LectureRepository;

    beforeEach(() => {
        lectureRepository = null;
    });

    it('findEntity by Mockito', async () => {
        const mockNodePgTemplateClass:NodePgTemplate = mock(NodePgTemplate);

        when(mockNodePgTemplateClass.query(anyString())).thenResolve([{
            name: "test",
            category: LectureCategory.WEB,
            student_count: 1,
            publish_status: 2,
        }]);

        lectureRepository = new LectureRepository(instance(mockNodePgTemplateClass));

        const result = await lectureRepository.findEntity(1);

        expect(result.name).toBe("test");
        expect(result.category).toBe(LectureCategory.WEB);
        expect(result.studentCount).toBe(1);
        expect(result.publishStatus).toBe(2);
    });

    it('findEntity By Stub', async () => {
        const mockNodePgTemplate:NodePgTemplate = new class extends NodePgTemplate {
            async query(sql: string): Promise<any[]> {
                return Promise.resolve([{
                    name: "test",
                    category: LectureCategory.WEB,
                    student_count: 1,
                    publish_status: 2,
                }]);
            }
        }
        lectureRepository = new LectureRepository(mockNodePgTemplate);

        const result = await lectureRepository.findEntity(1);

        expect(result.name).toBe("test");
        expect(result.category).toBe(LectureCategory.WEB);
        expect(result.studentCount).toBe(1);
        expect(result.publishStatus).toBe(2);
    });
});


