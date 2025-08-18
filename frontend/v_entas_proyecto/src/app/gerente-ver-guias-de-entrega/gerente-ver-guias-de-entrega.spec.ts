import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GerenteVerGuiasDeEntrega } from './gerente-ver-guias-de-entrega';

describe('GerenteVerGuiasDeEntrega', () => {
  let component: GerenteVerGuiasDeEntrega;
  let fixture: ComponentFixture<GerenteVerGuiasDeEntrega>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GerenteVerGuiasDeEntrega]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GerenteVerGuiasDeEntrega);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
