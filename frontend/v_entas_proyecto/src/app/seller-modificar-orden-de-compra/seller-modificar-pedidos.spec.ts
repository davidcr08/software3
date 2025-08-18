import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerModificarPedidos } from './seller-modificar-pedidos';

describe('SellerModificarPedidos', () => {
  let component: SellerModificarPedidos;
  let fixture: ComponentFixture<SellerModificarPedidos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SellerModificarPedidos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SellerModificarPedidos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
