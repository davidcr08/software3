import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TesoreroVerGuiaDeEntrega } from './tesorero-ver-guia-de-entrega';

describe('TesoreroVerGuiaDeEntrega', () => {
  let component: TesoreroVerGuiaDeEntrega;
  let fixture: ComponentFixture<TesoreroVerGuiaDeEntrega>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TesoreroVerGuiaDeEntrega]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TesoreroVerGuiaDeEntrega);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
