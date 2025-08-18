import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GerenteCrudSellers } from './gerente-crud-sellers';

describe('GerenteCrudSellers', () => {
  let component: GerenteCrudSellers;
  let fixture: ComponentFixture<GerenteCrudSellers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GerenteCrudSellers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GerenteCrudSellers);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
