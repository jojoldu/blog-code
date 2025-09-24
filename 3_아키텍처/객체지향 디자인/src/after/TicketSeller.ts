import {TicketOffice} from "./TicketOffice";
import {Audience} from "./Audience";

export class TicketSeller {
    private _ticketOffice: TicketOffice;

    constructor(ticketOffice: TicketOffice) {
        this._ticketOffice = ticketOffice;
    }

    public sellTo(audience: Audience): void {
        this.ticketOffice.sellTicketTo(audience);
    }

    get ticketOffice(): TicketOffice {
        return this._ticketOffice;
    }
}
