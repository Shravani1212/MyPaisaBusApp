import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private currentTheme = 'premium';

  setTheme(theme: string) {
    this.currentTheme = theme;
    document.body.setAttribute('data-theme', theme);
  }

  getTheme() {
    return this.currentTheme;
  }
}
