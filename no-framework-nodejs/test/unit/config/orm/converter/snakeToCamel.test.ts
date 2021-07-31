import {snakeToCamel} from "../../../../../src/config/orm/converter/snakeToCamel";

describe('SnakeCase to CamelCase', () => {
    it('name_case는 nameCase가 된다', () => {
        const result = snakeToCamel('name_case');
        expect(result).toBe('nameCase');
    });

    it('name_case_with는 nameCaseWith가 된다', () => {
        const result = snakeToCamel('name_case_with');
        expect(result).toBe('nameCaseWith');
    });
});
