export abstract class PageRequest {
    pageNo: number;
    pageSize: number;

    getPageQuery(): string {
        if(!this.getLimit() && !this.getOffset()) {
            return "";
        }

        return `LIMIT ${this.getLimit()} OFFSET ${this.getOffset()}`;
    }

    getOffset(): number {
        return (this.pageNo-1) * this.pageSize;
    }

    getLimit(): number {
        return this.pageSize;
    }

    getLimitWithNext(): number {
        return this.pageSize + 1;
    }

    getPageNo(): number {
        return this.pageNo? this.pageNo: 1;
    }

    getPageSize(): number {
        return this.pageSize? this.pageSize: 10;
    }
}
