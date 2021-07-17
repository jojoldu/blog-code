import {camelToSnake} from "../../../src/config/orm/camelToSnake";

describe('CamelCase to SnakeCase', () => {
    it('nameCase는 name_case가 된다', () => {
        const result = camelToSnake('nameCase');
        expect(result).toBe('name_case');
    });

    it('NameCase는 name_case가 된다', () => {
        const result = camelToSnake('NameCase');
        expect(result).toBe('name_case');
    });
});
