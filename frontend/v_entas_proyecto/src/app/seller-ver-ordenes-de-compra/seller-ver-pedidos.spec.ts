import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerVerPedidos } from './seller-ver-pedidos';

describe('SellerVerPedidos', () => {
  let component: SellerVerPedidos;
  let fixture: ComponentFixture<SellerVerPedidos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SellerVerPedidos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SellerVerPedidos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
