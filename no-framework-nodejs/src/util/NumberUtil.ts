
export class NumberUtil {
    static toNumber(str: string): number {
        const result = Number(str);
        if(isNaN(result)) {
            throw new Error(`숫자로 변환할 수 없는 문자열입니다. str=${str}`);
        }

        return result;
    }
}
