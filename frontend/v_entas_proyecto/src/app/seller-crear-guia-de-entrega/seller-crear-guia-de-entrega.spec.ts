import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerCrearGuiaDeEntrega } from './seller-crear-guia-de-entrega';

describe('SellerCrearGuiaDeEntrega', () => {
  let component: SellerCrearGuiaDeEntrega;
  let fixture: ComponentFixture<SellerCrearGuiaDeEntrega>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SellerCrearGuiaDeEntrega]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SellerCrearGuiaDeEntrega);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
