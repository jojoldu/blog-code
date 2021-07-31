export abstract class PageRequest {
    private _pageNo: number;
    private _pageSize: number;
    private _order: string;
    private _isAsc: boolean;

    setPageNoAndSize(pageNo: number, pageSize: number) {
        this._pageNo = pageNo? pageNo: 1;
        this._pageSize = pageSize? pageSize: 10;
    }

    setOrderBy(order: string, isAsc: boolean) {
        this._order = order;
        this._isAsc = isAsc;
    }

    getOrderByQuery(): string {
        if(!this._order) {
            return '';
        }

        const asc = this._isAsc? 'ASC' : 'DESC';
        return `ORDER BY ${this._order} ${asc}`;
    }

    getPageQuery(): string {
        if(!this.getLimit() && !this.getOffset()) {
            return "";
        }

        return `LIMIT ${this.getLimit()} OFFSET ${this.getOffset()}`;
    }

    getOffset(): number {
        return (this._pageNo-1) * this._pageSize;
    }

    getLimit(): number {
        return this._pageSize;
    }

    getLimitWithNext(): number {
        return this._pageSize + 1;
    }

    get order(): string {
        return this._order;
    }

    get isAsc(): boolean {
        return this._isAsc;
    }

    get pageNo(): number {
        return this._pageNo;
    }

    get pageSize(): number {
        return this._pageSize;
    }
}
