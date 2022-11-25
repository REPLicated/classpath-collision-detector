{
  description = "Nix devShell for building classpath-collision-detector gradle plugin";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, utils }:
    {
      overlay = final: prev: {
        jdk = prev.jdk8;
        gradle = (prev.gradle_7.override { java = final.jdk; });
      };
    }
    //
    utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          overlays = [ self.overlay ];
        };
      in
      {
        devShell = pkgs.mkShellNoCC {
          buildInputs = with pkgs; [ jdk gradle ];
        };

        formatter = pkgs.nixpkgs-fmt;
      });
}
