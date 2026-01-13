# Siloli Android Client

Aplikasi client Android untuk sistem pelaporan Siloli, dibangun menggunakan Kotlin dan Jetpack Compose.

## Persiapan Backend
Pastikan backend PHP sudah berjalan sebelum menjalankan aplikasi ini.

1.  Pastikan XAMPP (Apache & MySQL) sudah berjalan.
2.  Folder backend harus berada di `htdocs/siloli` di komputer host.
3.  Database `siloli` harus sudah dibuat dengan tabel `users` dan `reports`.

## Cara Membuka Project
1.  Buka **Android Studio**.
2.  Pilih **Open**, lalu navigasikan ke folder `d:\kuliah\semester 5\mobile\SiloliV2`.
3.  Tunggu hingga Gradle sync selesai.

## Konfigurasi Emulator (AVD)
Project ini dikonfigurasi untuk berjalan di Emulator Android standar.

1.  Buka **Tools > Device Manager**.
2.  Klik **Create Device**.
3.  Pilih **Phone > Medium Phone**.
4.  Pilih System Image **API 36** (atau VanillaIceCream/Android 15+ jika 36 belum tersedia, namun target SDK di set ke 36).
    *   *Note: Jika API 36 belum muncul, API 35 (Android 15) juga kompatibel.*
5.  Namakan device: `Siloli_Medium_API_36_1`.
6.  Finish.

### IP Address Backend
Aplikasi dikonfigurasi menggunakan Base URL `http://10.0.2.2/siloli/`.
Alamat IP `10.0.2.2` adalah alamat khusus emulator untuk mengakses `localhost` komputer host.

**Jika Anda menggunakan HP Asli (Real Device):**
1.  Pastikan HP dan Laptop terhubung ke Wi-Fi yang sama.
2.  Cari IP Address laptop (cmd: `ipconfig`). Contoh: `192.168.1.5`.
3.  Buka `app/src/main/java/com/example/siloli/network/NetworkModule.kt`.
4.  Ubah `BASE_URL` menjadi `http://192.168.1.5/siloli/`.

## Gradle Versions
Project ini menggunakan Gradle versi terbaru yang stabil. Jika Anda perlu menyamakan dengan versi teman (misal `QuestAPI_174`):
1.  Buka `gradle/libs.versions.toml`.
2.  Ubah versi pada bagian `[versions]`.
    *   Contoh: `agp = "8.1.0"`, `kotlin = "1.9.0"`.
3.  Sync Project.

## Cara Menggunakan Aplikasi (Acceptance Test)
1.  Jalankan aplikasi di Emulator.
2.  **Register**: Klik "Daftar Akun", masukkan username & password, klik Daftar.
3.  **Login**: Masukkan akun yang baru dibuat.
4.  **Buat Laporan**: Klik tombol (+) FAB, isi wilayah dan aduan, Simpan.
5.  **Lihat Laporan**: Laporan akan muncul di halaman utama.
6.  **Edit**: Klik icon Pensil pada laporan, ubah data, Simpan.
7.  **Hapus**: Klik icon Sampah, konfirmasi dialog "Ya".

## Troubleshooting
*   **Error: CLEARTEXT communication not permitted**: Sudah ditangani dangan `android:usesCleartextTraffic="true"` di AndroidManifest.xml.
*   **Connection Refused**: Pastikan XAMPP nyala dan alamat IP benar (10.0.2.2 untuk emulator).
