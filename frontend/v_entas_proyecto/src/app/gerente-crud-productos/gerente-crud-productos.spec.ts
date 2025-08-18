import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GerenteCrudProductos } from './gerente-crud-productos';

describe('GerenteCrudProductos', () => {
  let component: GerenteCrudProductos;
  let fixture: ComponentFixture<GerenteCrudProductos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GerenteCrudProductos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GerenteCrudProductos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
