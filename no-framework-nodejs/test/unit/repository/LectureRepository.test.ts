import {LectureRepository} from "../../../src/repository/lecture/LectureRepository";
import {NodePgTemplate} from "../../../src/config/database/NodePgTemplate";
import { anyOfClass, anyString, instance, mock, when } from "ts-mockito";
import {LectureCategory} from "../../../src/entity/lecture/LectureCategory";
import any = jasmine.any;
import { convert, LocalDateTime } from "@js-joda/core";

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

        const result = await lectureRepository.findOne(1);

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

        const result = await lectureRepository.findOne(1);

        expect(result.name).toBe("test");
        expect(result.category).toBe(LectureCategory.WEB);
        expect(result.studentCount).toBe(1);
        expect(result.publishStatus).toBe(2);
    });

    it('getLectureByDate', async () => {
        const mockNodePgTemplateClass:NodePgTemplate = mock(NodePgTemplate);

        when(mockNodePgTemplateClass.queryWith(anyString(), anyOfClass(Array))).thenResolve([{
            name: "test",
            category: LectureCategory.WEB,
            student_count: 1,
        }]);

        lectureRepository = new LectureRepository(instance(mockNodePgTemplateClass));

        const createdAt = LocalDateTime.of(2022,11,26, 12,0,5);
        const createdDate = convert(createdAt).toDate();
        const result = await lectureRepository.getLectureByDate(createdDate);

        expect(result.name).toBe("test");
        expect(result.category).toBe(LectureCategory.WEB);
        expect(result.studentCount).toBe(1);
    });

    it('getLectureByLocalDate', async () => {
        const mockNodePgTemplateClass:NodePgTemplate = mock(NodePgTemplate);

        when(mockNodePgTemplateClass.queryWith(anyString(), anyOfClass(Array))).thenResolve([{
            name: "test",
            category: LectureCategory.WEB,
            student_count: 1,
        }]);

        lectureRepository = new LectureRepository(instance(mockNodePgTemplateClass));

        const createdAt = LocalDateTime.of(2022,11,26, 12,0,5);
        const result = await lectureRepository.getLectureByLocalDate(createdAt);

        expect(result.name).toBe("test");
        expect(result.category).toBe(LectureCategory.WEB);
        expect(result.studentCount).toBe(1);
    });
});


