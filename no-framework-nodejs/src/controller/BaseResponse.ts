export class BaseResponse {
    readonly code:string;
    readonly message: string;
    readonly body: any;

    constructor(code: string, message: string, body: any) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    static OK (body: any) {
        return new BaseResponse("OK", "", body);
    }

    static ERROR (e: Error) {
        return new BaseResponse("ERROR", `${e.message}`, "");
    }

}
