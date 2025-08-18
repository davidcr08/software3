import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GerenteVerOrdenesDeEntrega } from './gerente-ver-ordenes-de-entrega';

describe('GerenteVerOrdenesDeEntrega', () => {
  let component: GerenteVerOrdenesDeEntrega;
  let fixture: ComponentFixture<GerenteVerOrdenesDeEntrega>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GerenteVerOrdenesDeEntrega]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GerenteVerOrdenesDeEntrega);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
