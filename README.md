# Ghidra SandBlaster Plugin

[![GitHub Release](https://img.shields.io/github/release/shark4ce/Ghidra-SandBlaster-Plugin.svg)](https://github.com/shark4ce/Ghidra-SandBlaster-Plugin/releases/)


The SandBlasterPlugin for Ghidra is a specialized and sophisticated extension that leverages the functionalities of the [SandBlaster](https://github.com/malus-security/sandblaster.git) tool to facilitate the streamlined analysis and reverse engineering of iOS sandbox profiles. This tool is essential in converting the profiles from their binary format into a human-readable SBPL (Sandbox Profile Language) format, directly within the Ghidra environment. The iOS operating system employs a security feature known as the sandbox, which imposes restrictions on applications, limiting their access to certain parts of the system or user data. These restrictions are delineated in sandbox profiles, typically stored in a binary format. The SandBlaster Plugin simplifies the analysis process by converting these binary profiles into the readable SBPL format, thereby aiding researchers and security analysts in comprehending and assessing the security implications of these profiles.


## Prerequisites

- Ghidra
- Python 2
- Python 3
- pip (Python package installer for Python 3)
- `lief` package for Python 3

The plugin handles the installation of additional Python 3 required packages, such as `lief`.

## Submodules

This project includes the [SandBlaster](https://github.com/malus-security/sandblaster.git) submodule, which requires Python 2, Python 3, pip, and the `lief` package for Python 3.

## Installation

1. Download Release version of extension and install it in Ghidra in Ghidra in `File → Install Extensions...` `OR`

1. Clone this repository:

    ```
    git clone https://github.com/yourusername/ghidra-plugin.git
    cd ghidra-plugin
    ```

2. Initialize and update the submodules:

    ```
    git submodule init
    git submodule update
    ```

3. Build the Ghidra plugin:

    ```
    ./gradlew build
    ```

    This will generate a `.zip` file in the `dist` directory.

4. Once built, the plugin can be installed in Ghidra in `File → Install Extensions...`

## Usage

Provide detailed instructions on how to use the plugin.

1. Start Ghidra.
2. Open `File → Configure → Miscellaneous` and enable `SandBlasterPlugin`
3. Open `Window → SandBlasterPlugin` and the main window of plugin will be displayed
4. Specify `Python2` and `Python3` bin paths or use `Auto Detect` action
5. Specify a valid `iOS Version` and the `SandBox Operations File Source` and `SandBox Profiles File Source`
6. `Start` process
7. View reversed SandBox profiles in the `Result` tab
8. Check the logs

## Releases

The built version of the Ghidra plugin can be found in the [releases](https://github.com/shark4ce/Ghidra-SandBlaster-Plugin/releases/) section of this repository.

## Contributing

If you are interested in contributing to this project, please read the [CONTRIBUTING.md](CONTRIBUTING.md) file.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
