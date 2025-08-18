import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerCrearPedidos } from './seller-crear-pedidos';

describe('SellerCrearPedidos', () => {
  let component: SellerCrearPedidos;
  let fixture: ComponentFixture<SellerCrearPedidos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SellerCrearPedidos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SellerCrearPedidos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
