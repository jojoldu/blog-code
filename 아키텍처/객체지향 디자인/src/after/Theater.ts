import {Audience} from "./Audience";
import {TicketSeller} from "./TicketSeller";

export class Theater {
    private ticketSeller: TicketSeller;

    constructor(ticketSeller: TicketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public enter(audience: Audience): void {
        this.ticketSeller.sellTo(audience);
    }
}
