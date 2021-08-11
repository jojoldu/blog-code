import {Audience} from "./Audience";
import {TicketSeller} from "./TicketSeller";

export class Theater {
    private ticketSeller: TicketSeller;

    constructor(ticketSeller: TicketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public enter(audience: Audience): void {
        if (audience.bag.hasInvitation()) {
            const ticket = this.ticketSeller.ticketOffice.ticket;
            audience.bag.setTicket(ticket);
        } else {
            const ticket = this.ticketSeller.ticketOffice.ticket;
            audience.bag.minusAmount(ticket.fee);
            this.ticketSeller.ticketOffice.plusAmount(ticket.fee);
            audience.bag.setTicket(ticket);
        }
    }
}
