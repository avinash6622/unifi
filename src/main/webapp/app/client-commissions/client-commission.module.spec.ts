import { ClientCommissionModule } from './client-commission.module';

describe('ClientCommissionModule', () => {
    let clientCommissionModule: ClientCommissionModule;

    beforeEach(() => {
        clientCommissionModule = new ClientCommissionModule();
    });

    it('should create an instance', () => {
        expect(clientCommissionModule).toBeTruthy();
    });
});
